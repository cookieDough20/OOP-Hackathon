# RideSync Solutions - Quick Start Guide 🚀

## Prerequisites
- Java 17 or higher
- Maven 3.6+

## 30-Second Start

```bash
# 1. Build the project
mvn clean package -DskipTests

# 2. Run the application
cd ridesync-api
mvn spring-boot:run

# 3. Open in browser
# Swagger UI: http://localhost:8080/swagger-ui.html
```

## Application URLs

| URL | Description |
|-----|-------------|
| http://localhost:8080/swagger-ui.html | Interactive API Documentation |
| http://localhost:8080/h2-console | Database Console (JDBC: `jdbc:h2:mem:ridesyncdb`, User: `sa`) |
| http://localhost:8080/actuator/health | Health Check |
| http://localhost:8080/api/dashboard | Analytics Dashboard |
| http://localhost:8080/api/dashboard/flowchart | System Flowchart |

## Pre-Seeded Data

The application automatically creates:
- **5 Drivers** with different vehicle types
- **10 Riders** ready to book rides

Get the IDs:
```bash
curl http://localhost:8080/api/riders | jq '.[0:3]'
curl http://localhost:8080/api/drivers | jq '.[0:3]'
```

## Book Your First Ride

### Using Swagger UI (Easiest)
1. Open http://localhost:8080/swagger-ui.html
2. Find **POST /api/bookRide**
3. Click "Try it out"
4. Replace `<RIDER_ID>` with an actual rider ID
5. Click "Execute"

### Using cURL

```bash
# Get a rider ID first
RIDER_ID=$(curl -s http://localhost:8080/api/riders | jq -r '.[0].id')

# Book a Standard ride
curl -X POST http://localhost:8080/api/bookRide \
  -H "Content-Type: application/json" \
  -d "{
    \"riderId\": \"$RIDER_ID\",
    \"startLatitude\": 40.7128,
    \"startLongitude\": -74.0060,
    \"endLatitude\": 40.7580,
    \"endLongitude\": -73.9855,
    \"rideType\": \"STANDARD\"
  }" | jq
```

## Run Automated Demo

```bash
# Make executable (if needed)
chmod +x demo-script.sh

# Run demo
./demo-script.sh
```

This will:
- Book 3 concurrent rides (Standard, Pool, Luxury)
- Start and complete all rides
- Show analytics dashboard
- Display top earning drivers

## Test Different Ride Types

### Standard Ride (Regular pricing)
```json
{
  "rideType": "STANDARD"
}
```

### Pool Ride (30% cheaper!)
```json
{
  "rideType": "POOL"
}
```

### Luxury Ride (50% premium)
```json
{
  "rideType": "LUXURY"
}
```

## View Analytics

```bash
# Dashboard with comprehensive stats
curl http://localhost:8080/api/dashboard | jq

# Top earning drivers
curl http://localhost:8080/api/analytics/top-drivers | jq

# Filter rides by status
curl 'http://localhost:8080/api/analytics/rides?status=COMPLETED' | jq
```

## Explore Features

### 1. Dynamic Surge Pricing
- Prices vary by time of day, location, and demand
- Book same route multiple times to see variations

### 2. Real-Time Updates (WebSocket)
Connect to: `ws://localhost:8080/ws/rides`

### 3. Database Queries
1. Open http://localhost:8080/h2-console
2. JDBC URL: `jdbc:h2:mem:ridesyncdb`
3. Username: `sa`, Password: (empty)
4. Try: `SELECT * FROM RIDES WHERE STATUS = 'COMPLETED'`

### 4. System Flowchart
```bash
curl http://localhost:8080/api/dashboard/flowchart
```

### 5. Reflection Demo
```bash
curl http://localhost:8080/api/dashboard/reflection/ride | jq
```

## Common Operations

### Complete a Ride
```bash
# Get ride ID from booking response
RIDE_ID="<your-ride-id>"

# Start the ride
curl -X POST http://localhost:8080/api/startRide/$RIDE_ID

# Complete the ride
curl -X POST http://localhost:8080/api/completeRide/$RIDE_ID | jq
```

### Check Driver Earnings
```bash
# Get driver ID
DRIVER_ID=$(curl -s http://localhost:8080/api/drivers | jq -r '.[0].id')

# View earnings
curl http://localhost:8080/api/analytics/earnings/$DRIVER_ID | jq
```

## Troubleshooting

### Port 8080 already in use
Edit `ridesync-api/src/main/resources/application.yml`:
```yaml
server:
  port: 8081  # Change to any available port
```

### Application won't start
```bash
# Check Java version
java -version  # Should be 17+

# Rebuild
mvn clean install -DskipTests
```

### No riders/drivers found
The data is seeded automatically on startup. If empty:
- Restart the application
- Check logs for errors

## Next Steps

1. ✅ Book a few rides
2. ✅ Complete them and check earnings
3. ✅ View analytics dashboard
4. ✅ Explore Swagger UI for all endpoints
5. ✅ Try concurrent bookings
6. ✅ Check H2 database console

## For Hackathon Judges

See **DEMO.md** for the complete 5-minute demo script.

---

**Need help?** Check README.md for full documentation.

**Enjoy using RideSync Solutions!** 🎉
