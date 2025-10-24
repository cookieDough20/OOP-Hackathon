#!/bin/bash

# RideSync Solutions - Demo Script
# This script demonstrates the complete ride booking flow

echo "🚗 RideSync Solutions - Hackathon Demo Script"
echo "=============================================="
echo ""

BASE_URL="http://localhost:8080"

# Colors for output
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo -e "${BLUE}Step 1: Fetching all available drivers...${NC}"
curl -s "$BASE_URL/api/drivers/available" | jq '.'
echo ""
echo ""

echo -e "${BLUE}Step 2: Fetching all riders...${NC}"
RIDERS=$(curl -s "$BASE_URL/api/riders" | jq -r '.[0:3] | .[].id')
echo "Found riders: $RIDERS"
echo ""
echo ""

# Get first rider ID
RIDER_ID=$(echo $RIDERS | awk '{print $1}')

echo -e "${BLUE}Step 3: Booking a STANDARD ride...${NC}"
RIDE1=$(curl -s -X POST "$BASE_URL/api/rides/book" \
  -H "Content-Type: application/json" \
  -d "{
    \"riderId\": \"$RIDER_ID\",
    \"startLatitude\": 12.9716,
    \"startLongitude\": 77.5946,
    \"startAddress\": \"MG Road, Bangalore\",
    \"endLatitude\": 12.9698,
    \"endLongitude\": 77.7500,
    \"endAddress\": \"Whitefield, Bangalore\",
    \"rideType\": \"STANDARD\"
  }")

RIDE1_ID=$(echo $RIDE1 | jq -r '.rideId')
echo -e "${GREEN}✓ Ride booked: $RIDE1_ID${NC}"
echo $RIDE1 | jq '.'
echo ""
echo ""

echo -e "${BLUE}Step 4: Starting the ride...${NC}"
curl -s -X POST "$BASE_URL/api/rides/$RIDE1_ID/start" | jq '.'
echo ""
echo ""

echo -e "${BLUE}Step 5: Completing the ride...${NC}"
curl -s -X POST "$BASE_URL/api/rides/$RIDE1_ID/complete" | jq '.'
echo ""
echo ""

echo -e "${BLUE}Step 6: Viewing Dashboard Analytics...${NC}"
curl -s "$BASE_URL/api/analytics/dashboard" | jq '.'
echo ""
echo ""

echo -e "${BLUE}Step 7: Viewing Top Drivers...${NC}"
curl -s "$BASE_URL/api/analytics/top-drivers?limit=3" | jq '.'
echo ""
echo ""

echo -e "${BLUE}Step 8: Viewing System Flowchart...${NC}"
curl -s "$BASE_URL/api/metadata/flowchart" | jq -r '.asciiArt'
echo ""
echo ""

echo -e "${GREEN}✨ Demo completed successfully!${NC}"
echo "=============================================="
echo "Visit http://localhost:8080/swagger-ui.html for interactive API docs"
