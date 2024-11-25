package com.aladin.util.subway.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;

import com.aladin.util.subway.dto.SubwayCongestResponseDto;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SubwayService {
	private static final double TRANSFER_WEIGHT = 1.1; // 기본 환승역 가중치
	private static final double MULTI_TRANSFER_WEIGHT = 1.2; // 3개 이상 노선이 교차하는 환승역 가중치
	private static final double SUSTAINED_THRESHOLD = 3;
	private String subwayCsvFile = "src/main/resources/csv/subway.csv";
	private static List<PathData> stations = new ArrayList<>();

	public static class PathData {
		private String line; // 호선 정보
		private String from;
		private String to;
		private double morningCongestion; // 출근 혼잡도
		private double eveningCongestion; // 퇴근 혼잡도
		private int lineCount; // 해당 역이 속한 호선 수

		public PathData(String line, String from, String to, double morningCongestion, double eveningCongestion, int lineCount) {
			this.line = line;
			this.from = from;
			this.to = to;
			this.morningCongestion = morningCongestion;
			this.eveningCongestion = eveningCongestion;
			this.lineCount = lineCount;
		}

		public String getLine() {
			return line;
		}

		public String getFrom() {
			return from;
		}

		public String getTo() {
			return to;
		}

		public double getMorningTrafficjam() {
			return morningCongestion;
		}

		public double getEveningTrafficjam() {
			return eveningCongestion;
		}

		public int getLineCount() {
			return lineCount;
		}
	}

	// 파일 데이터 전부 읽어오기
	@PostConstruct
	public void readCSVFile() {
		try (BufferedReader reader = new BufferedReader(new FileReader(subwayCsvFile));
				CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader())) {

			for (CSVRecord record : csvParser) {
				String line = record.get("line");
				String from = record.get("from");
				String to = record.get("to");
				double morningTrafficjam = Double.parseDouble(record.get("goWork"));
				double eveningTrafficjam = Double.parseDouble(record.get("leaveWork"));
				int lineCount = Integer.parseInt(record.get("lineCount"));
				stations.add(new PathData(line, from, to, morningTrafficjam, eveningTrafficjam, lineCount));
			}
		} catch (IOException e) {
			throw new IllegalStateException("Cannot initialize SubwayService due to file load failure.", e);
		}
	}

	// 집 -> 회사 순으로 지하철 이름 제공시
	// 집 -> 회사 순으로 출근 시간대 혼잡도 계산
	// 회사 -> 집 순으로 퇴근 시간대 혼잡도 계산
	public SubwayCongestResponseDto calculateTrafficJam(String[] stationNames) {
		List<Double> morningCongests = new ArrayList<>(); // 아침 혼잡도 리스트
		List<Double> eveningCongests = new ArrayList<>(); // 저녁 혼잡도 리스트
		double totalCongest = 0;

		int sustainedMorningCongestionCount = 0; // 연속 혼잡 구간 카운트
		int sustainedEveningCongestionCount = 0; // 연속 혼잡 구간 카운트

		for (int i = 0; i < stationNames.length - 1; i++) {
			// 아침 경로
			String morningFrom = stationNames[i];
			String morningTo = stationNames[i + 1];

			// 저녁 경로 (역순)
			String eveningFrom = stationNames[stationNames.length - 1 - i];
			String eveningTo = stationNames[stationNames.length - 2 - i];
			// 아침 혼잡도 계산
			Optional<PathData> morningPath = stations.stream().filter(s -> s.getFrom().equals(morningFrom) && s.getTo().equals(morningTo))
					.findFirst();

			if (morningPath.isPresent()) {
				PathData path = morningPath.get();
				double congestion = path.getMorningTrafficjam();
				double transferWeight = getTransferWeight(path); // 환승 가중치 적용
				congestion *= transferWeight;

				// 연속 혼잡 구간 가중치 적용
				if (congestion > SUSTAINED_THRESHOLD) {
					sustainedMorningCongestionCount++;
					double sustainedWeight = 1 + (0.05 * sustainedMorningCongestionCount);
					congestion *= sustainedWeight;
				} else {
					sustainedMorningCongestionCount = 0; // 기준 미달이면 리셋
				}
				log.info("{} >> {}: {}", morningFrom, morningTo, congestion);
				morningCongests.add(congestion); // 리스트에 저장
				totalCongest += congestion;
			} else {
				throw new IllegalArgumentException("존재하지 않는 경로입니다: " + morningFrom + " to " + morningTo);
			}

			// 저녁 혼잡도 계산
			Optional<PathData> eveningPath = stations.stream().filter(s -> s.getFrom().equals(eveningFrom) && s.getTo().equals(eveningTo))
					.findFirst();

			if (eveningPath.isPresent()) {
				PathData path = eveningPath.get();
				double congestion = path.getEveningTrafficjam();
				double transferWeight = getTransferWeight(path); // 환승 가중치 적용
				congestion *= transferWeight;

				// 연속 혼잡 구간 가중치 적용
				if (congestion > SUSTAINED_THRESHOLD) {
					sustainedEveningCongestionCount++;
					double sustainedWeight = 1 + (0.05 * sustainedEveningCongestionCount);
					congestion *= sustainedWeight;
				} else {
					sustainedEveningCongestionCount = 0; // 기준 미달이면 리셋
				}
				log.info("{} >> {}: {}", eveningFrom, eveningTo, congestion);
				eveningCongests.add(congestion); // 리스트에 저장
				totalCongest += congestion;
			} else {
				throw new IllegalArgumentException("존재하지 않는 경로입니다: " + eveningFrom + " to " + eveningTo);
			}
		}

		// 각 리스트 출력 (디버깅용)
//		System.out.println("Morning Congestion Datas: " + morningCongestionDatas);
//		System.out.println("Evening Congestion Datas: " + eveningCongestionDatas);

		// 경로 수만큼 나눠 평균 계산
		totalCongest /= (stationNames.length - 1) * 2;
		return new SubwayCongestResponseDto(morningCongests, eveningCongests, totalCongest);
	}

	private static double getTransferWeight(PathData station) {
		if (station.getLineCount() >= 3) {
			return MULTI_TRANSFER_WEIGHT;
		}
		return station.getLineCount() >= 2 ? TRANSFER_WEIGHT : 1.0;
	}

//	public static void main(String[] args) {
//		SubwayService service = new SubwayService();
//		service.readCSVFile("src/main/resources/csv/subway.csv");
//		String[] inputStations = { "사당역", "방배역", "서초역", "교대역", "강남역", "역삼역" };
//		double morningTrafficJam = service.calculateTrafficJam(inputStations);
//		System.out.println("Total Morning Traffic Jam: " + morningTrafficJam / inputStations.length);
//
//	}
}
