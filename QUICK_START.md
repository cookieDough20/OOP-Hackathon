# ⚡ RideSync Solutions - Quick Start Guide

## 🎯 Goal
Get the application running in under 2 minutes!

## 📋 Prerequisites Check

```bash
# Check Java (need 17+)
java -version
# Should show: version "17" or higher

# Check Maven (need 3.8+)
mvn -version
# Should show: Apache Maven 3.8 or higher
```

**Don't have them?**
- Java: https://adoptium.net/ (get JDK 17+)
- Maven: https://maven.apache.org/download.cgi

## 🚀 Start in 3 Steps

### Step 1: Build
```bash
cd workspace
mvn clean install -DskipTests
```
_Takes ~30-60 seconds first time (downloads dependencies)_

### Step 2: Run
```bash
cd ridesync-api
mvn spring-boot:run
```
_Wait for: "Started RideSyncApplication in X seconds"_

### Step 3: Test
Open browser: **http://localhost:8080/swagger-ui.html**

You should see the Swagger UI with all API endpoints! 🎉

## 🧪 Quick Test - Book Your First Ride

### Option A: Using Swagger UI (Easiest)

1. Open **http://localhost:8080/swagger-ui.html**
2. Expand **"Rider Management"** → Click **POST /api/riders**
3. Click **"Try it out"**
4. Copy this JSON:
```json
{
  "name": "Test User",
  "phone": "9999999999",
  "latitude": 12.9716,
  "longitude": 77.5946,
  "address": "MG Road"
}
```
5. Click **"Execute"** - Copy the `riderId` from response
6. Expand **"Ride Operations"** → Click **POST /api/rides/book**
7. Click **"Try it out"**
8. Paste this (replace YOUR_RIDER_ID):
```json
{
  "riderId": "YOUR_RIDER_ID",
  "startLatitude": 12.9716,
  "startLongitude": 77.5946,
  "startAddress": "MG Road",
  "endLatitude": 12.9698,
  "endLongitude": 77.7500,
  "endAddress": "Whitefield",
  "rideType": "STANDARD"
}
```
9. Click **"Execute"** - See your ride booked! 🚗

### Option B: Using cURL (Terminal)

```bash
# 1. Get a rider ID
RIDER_ID=$(curl -s http://localhost:8080/api/riders | jq -r '.[0].id')
echo "Using rider: $RIDER_ID"

# 2. Book a ride
curl -X POST http://localhost:8080/api/rides/book \
  -H "Content-Type: application/json" \
  -d "{
    \"riderId\": \"$RIDER_ID\",
    \"startLatitude\": 12.9716,
    \"startLongitude\": 77.5946,
    \"endLatitude\": 12.9698,
    \"endLongitude\": 77.7500,
    \"rideType\": \"STANDARD\"
  }" | jq
```

## 📊 View Analytics

```bash
# Dashboard
curl http://localhost:8080/api/analytics/dashboard | jq

# Top drivers
curl http://localhost:8080/api/analytics/top-drivers?limit=5 | jq
```

## 🎨 Explore More

### H2 Database Console
1. Open: http://localhost:8080/h2-console
2. JDBC URL: `jdbc:h2:mem:ridesyncdb`
3. Username: `sa`
4. Password: (leave blank)
5. Click "Connect"
6. Run: `SELECT * FROM RIDES;`

### System Flowchart
```bash
curl http://localhost:8080/api/metadata/flowchart | jq -r '.asciiArt'
```

### Health Check
```bash
curl http://localhost:8080/actuator/health | jq
```

## 🐛 Troubleshooting

### Port 8080 already in use?
```bash
# Kill existing process
lsof -ti:8080 | xargs kill -9

# Or use different port
mvn spring-boot:run -Dserver.port=8081
```

### Build fails?
```bash
# Clean everything
mvn clean
rm -rf ~/.m2/repository/com/ridesync

# Rebuild
mvn clean install -DskipTests -U
```

### Application won't start?
Check logs for:
- Java version (need 17+)
- Port conflicts
- Maven repository issues

## 📚 Next Steps

1. ✅ **Read full README.md** - Complete documentation
2. ✅ **Check DEMO_INSTRUCTIONS.md** - 5-minute demo guide
3. ✅ **View PROJECT_SUMMARY.md** - Technical details
4. ✅ **Run tests**: `mvn test`
5. ✅ **Generate coverage**: `mvn test jacoco:report`

## 🎬 Automated Demo Script

```bash
chmod +x demo-script.sh
./demo-script.sh
```

Runs through a complete demo automatically!

## 💡 Pro Tips

- Use **Swagger UI** for interactive testing
- Check **H2 Console** to see database in real-time
- Monitor **Actuator** for application health
- Read **code comments** for learning

## 🆘 Need Help?

- Check README.md for detailed docs
- Review PROJECT_SUMMARY.md for architecture
- Examine code comments in source files
- All endpoints documented in Swagger UI

---

**You're all set! Start building your hackathon demo! 🚀**
