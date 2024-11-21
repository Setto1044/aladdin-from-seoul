function getPropertyById(id) {
  // 데이터베이스, API, 또는 로컬 저장소에서 데이터를 가져옴
  const properties = [
    {
      id: 2,
      title: 'Card 2',
      description: 'Beautiful 2-bedroom apartment.',
      detail: 'Spacious and modern apartment with all amenities included.',
      address: '456 Elm St, City',
      price: 2000,
      houseSize: 60.0,
      pricePer: 'month',
      latitude: '37.7849',
      longitude: '-122.4294',
      rentFrom: '2024-02-01',
      rentTo: '2024-12-31',
      createdAt: '2024-11-21T01:00:00Z',
      tags: ['#Balcony', '#Elevator', '#Balcony', '#Elevator', '#Balcony', '#Elevator'],
      views: 250,
      hostId: 124,
      imageUrls: [
        'https://cdn.class101.net/images/3e1377ef-0370-454f-8829-251668e8d7bd',
        'https://health.chosun.com/site/data/img_dir/2023/01/10/2023011001501_0.jpg',
      ],
      comments: ['Spacious rooms!', 'Great neighborhood!'],
    },
    {
      id: 3,
      title: 'Card 3',
      description: 'Cozy studio in downtown.',
      detail: 'Ideal for single professionals or students.',
      address: '789 Maple Ave, City',
      price: 1200,
      houseSize: 35.0,
      pricePer: 'month',
      latitude: '37.7649',
      longitude: '-122.4094',
      rentFrom: '2024-03-01',
      rentTo: '2024-12-31',
      createdAt: '2024-11-20T03:00:00Z',
      tags: ['#Balcony', '#Elevator', '#Balcony', '#Elevator', '#Balcony', '#Elevator'],
      views: 150,
      hostId: 125,
      imageUrls: ['https://example.com/image4.jpg'],
      comments: ['Perfect for short stays.', 'Love the view!'],
    },
  ]
  console.log(properties.find((property) => property.id == id))
  return properties.find((property) => property.id == id)
}

export default getPropertyById
