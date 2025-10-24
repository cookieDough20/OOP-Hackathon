#!/bin/bash

echo "╔══════════════════════════════════════════════════════╗"
echo "║     RideSync Solutions - Demo Script                ║"
echo "║     5-Minute Hackathon Demo                          ║"
echo "╚══════════════════════════════════════════════════════╝"
echo ""

BASE_URL="http://localhost:8080"

echo "Waiting for application to start..."
sleep 5

echo ""
echo "Step 1: Fetching pre-seeded riders and drivers..."
echo "------------------------------------------------------"

# Get first rider
RIDER_ID=$(curl -s $BASE_URL/api/riders | jq -r '.[0].id')
RIDER_ID_2=$(curl -s $BASE_URL/api/riders | jq -r '.[1].id')
RIDER_ID_3=$(curl -s $BASE_URL/api/riders | jq -r '.[2].id')

echo "✓ Rider 1 ID: $RIDER_ID"
echo "✓ Rider 2 ID: $RIDER_ID_2"
echo "✓ Rider 3 ID: $RIDER_ID_3"

echo ""
echo "Step 2: Booking Ride #1 (STANDARD)"
echo "------------------------------------------------------"
RIDE_1=$(curl -s -X POST $BASE_URL/api/bookRide \
  -H "Content-Type: application/json" \
  -d "{
    \"riderId\": \"$RIDER_ID\",
    \"startLatitude\": 40.7128,
    \"startLongitude\": -74.0060,
    \"endLatitude\": 40.7580,
    \"endLongitude\": -73.9855,
    \"rideType\": \"STANDARD\"
  }")

RIDE_ID_1=$(echo $RIDE_1 | jq -r '.rideId')
echo "✓ Ride booked!"
echo $RIDE_1 | jq '{rideId, driverName, estimatedFare, surgeMultiplier, message}'

echo ""
echo "Step 3: Booking Ride #2 (POOL - cheaper!)"
echo "------------------------------------------------------"
RIDE_2=$(curl -s -X POST $BASE_URL/api/bookRide \
  -H "Content-Type: application/json" \
  -d "{
    \"riderId\": \"$RIDER_ID_2\",
    \"startLatitude\": 40.7489,
    \"startLongitude\": -73.9680,
    \"endLatitude\": 40.7614,
    \"endLongitude\": -73.9776,
    \"rideType\": \"POOL\"
  }")

RIDE_ID_2=$(echo $RIDE_2 | jq -r '.rideId')
echo "✓ Ride booked!"
echo $RIDE_2 | jq '{rideId, driverName, estimatedFare, surgeMultiplier, message}'

echo ""
echo "Step 4: Booking Ride #3 (LUXURY - premium!)"
echo "------------------------------------------------------"
RIDE_3=$(curl -s -X POST $BASE_URL/api/bookRide \
  -H "Content-Type: application/json" \
  -d "{
    \"riderId\": \"$RIDER_ID_3\",
    \"startLatitude\": 40.7580,
    \"startLongitude\": -73.9855,
    \"endLatitude\": 40.7306,
    \"endLongitude\": -73.9352,
    \"rideType\": \"LUXURY\"
  }")

RIDE_ID_3=$(echo $RIDE_3 | jq -r '.rideId')
echo "✓ Ride booked!"
echo $RIDE_3 | jq '{rideId, driverName, estimatedFare, surgeMultiplier, message}'

echo ""
echo "Step 5: Starting and Completing Rides..."
echo "------------------------------------------------------"

# Start ride 1
curl -s -X POST $BASE_URL/api/startRide/$RIDE_ID_1 > /dev/null
echo "✓ Ride #1 started"

# Complete ride 1
COMPLETED_1=$(curl -s -X POST $BASE_URL/api/completeRide/$RIDE_ID_1)
echo "✓ Ride #1 completed"
echo $COMPLETED_1 | jq '{rideId, status, actualFare, message}'

# Start and complete ride 2
curl -s -X POST $BASE_URL/api/startRide/$RIDE_ID_2 > /dev/null
curl -s -X POST $BASE_URL/api/completeRide/$RIDE_ID_2 > /dev/null
echo "✓ Ride #2 completed"

# Start and complete ride 3
curl -s -X POST $BASE_URL/api/startRide/$RIDE_ID_3 > /dev/null
curl -s -X POST $BASE_URL/api/completeRide/$RIDE_ID_3 > /dev/null
echo "✓ Ride #3 completed"

echo ""
echo "Step 6: Analytics Dashboard"
echo "------------------------------------------------------"
DASHBOARD=$(curl -s $BASE_URL/api/dashboard)
echo $DASHBOARD | jq '{
  totalRides,
  completedRides,
  activeRides,
  totalRevenue,
  averageFare,
  totalDrivers,
  availableDrivers,
  ridesByType,
  topDrivers: .topDrivers[0:3]
}'

echo ""
echo "Step 7: Top Earning Drivers"
echo "------------------------------------------------------"
curl -s $BASE_URL/api/analytics/top-drivers?limit=5 | jq '.[] | {name, earnings, totalRides, rating}'

echo ""
echo "╔══════════════════════════════════════════════════════╗"
echo "║     Demo Completed Successfully! 🎉                  ║"
echo "╚══════════════════════════════════════════════════════╝"
echo ""
echo "Additional URLs to explore:"
echo "  • Swagger UI: $BASE_URL/swagger-ui.html"
echo "  • H2 Console: $BASE_URL/h2-console"
echo "  • Flowchart:  $BASE_URL/api/dashboard/flowchart"
echo "  • Health:     $BASE_URL/actuator/health"
echo ""
