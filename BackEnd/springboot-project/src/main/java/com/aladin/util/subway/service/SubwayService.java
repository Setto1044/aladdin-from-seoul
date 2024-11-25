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

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubwayService {

	private String subwayCsvFile = "src/main/resources/csv/subway.csv";

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Station {
		private String from;
		private String to;
		private double morningCongestion; // 출근 혼잡도
		private double eveningCongestion; // 퇴근 혼잡도
	}

	public List<Station> readCSVFile(String filePath) {
		List<Station> stations = new ArrayList<>();

		try (BufferedReader reader = new BufferedReader(new FileReader(filePath)); CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader())) {

			for (CSVRecord record : csvParser) {
				String from = record.get("from");
				String to = record.get("to");
				double morningTrafficjam = Double.parseDouble(record.get("goWork"));
				double eveningTrafficjam = Double.parseDouble(record.get("leaveWork"));
				stations.add(new Station(from, to, morningTrafficjam, eveningTrafficjam));
			}
		} catch (IOException e) {
			System.err.println("Error reading the file: " + e.getMessage());
		}

		return stations;
	}

	public double calculateTrafficJam(String[] stationNames, String timeType) {
		List<Station> stations = readCSVFile(subwayCsvFile);
		double totalTrafficJam = 0;

		for (int i = 0; i < stationNames.length - 1; i++) {
			String from = stationNames[i];
			String to = stationNames[i + 1];
			Optional<Station> match = stations.stream().filter(s -> s.getFrom().equals(from) && s.getTo().equals(to)).findFirst();

			if (match.isPresent()) {
				if ("morning".equalsIgnoreCase(timeType)) {
					totalTrafficJam += match.get().getMorningTrafficjam();
				} else if ("evening".equalsIgnoreCase(timeType)) {
					totalTrafficJam += match.get().getEveningTrafficjam();
				} else {
					System.err.println("Invalid time type: " + timeType);
				}
			} else {
				System.err.println("No data found for route from " + from + " to " + to);
			}
		}

		return totalTrafficJam;
	}

	public static void main(String[] args) {
		SubwayService service = new SubwayService();
		String[] inputStations = { "범계역", "평촌역", "인덕원역" };
		double morningTrafficJam = service.calculateTrafficJam(inputStations, "morning");
		System.out.println("Total Morning Traffic Jam: " + morningTrafficJam);

		double eveningTrafficJam = service.calculateTrafficJam(inputStations, "evening");
		System.out.println("Total Evening Traffic Jam: " + eveningTrafficJam);
	}
}
