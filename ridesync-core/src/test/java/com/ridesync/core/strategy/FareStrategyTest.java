package com.ridesync.core.strategy;

import com.ridesync.core.model.Location;
import com.ridesync.core.model.StandardRide;
import com.ridesync.core.model.PoolRide;
import com.ridesync.core.model.LuxuryRide;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Fare Strategy Tests")
class FareStrategyTest {
    
    @Test
    @DisplayName("Standard fare should be calculated correctly")
    void shouldCalculateStandardFare() {
        StandardRide ride = new StandardRide();
        ride.setStartLocation(new Location(40.7128, -74.0060));
        ride.setEndLocation(new Location(40.7580, -73.9855));
        
        FareStrategy strategy = new StandardFareStrategy();
        double fare = strategy.calculateFare(ride, 5.0, 1.0);
        
        assertThat(fare).isGreaterThan(0);
        assertThat(fare).isGreaterThanOrEqualTo(strategy.getMinimumFare());
    }
    
    @Test
    @DisplayName("Pool fare should be cheaper than standard")
    void poolFareShouldBeCheaper() {
        StandardRide standardRide = new StandardRide();
        PoolRide poolRide = new PoolRide();
        
        double distance = 10.0;
        FareStrategy standardStrategy = new StandardFareStrategy();
        FareStrategy poolStrategy = new PoolFareStrategy();
        
        double standardFare = standardStrategy.calculateFare(standardRide, distance, 1.0);
        double poolFare = poolStrategy.calculateFare(poolRide, distance, 1.0);
        
        assertThat(poolFare).isLessThan(standardFare);
    }
    
    @Test
    @DisplayName("Luxury fare should be higher than standard")
    void luxuryFareShouldBeHigher() {
        StandardRide standardRide = new StandardRide();
        LuxuryRide luxuryRide = new LuxuryRide();
        
        double distance = 10.0;
        FareStrategy standardStrategy = new StandardFareStrategy();
        FareStrategy luxuryStrategy = new LuxuryFareStrategy();
        
        double standardFare = standardStrategy.calculateFare(standardRide, distance, 1.0);
        double luxuryFare = luxuryStrategy.calculateFare(luxuryRide, distance, 1.0);
        
        assertThat(luxuryFare).isGreaterThan(standardFare);
    }
    
    @Test
    @DisplayName("Surge multiplier should increase fare")
    void surgeShouldIncreaseFare() {
        StandardRide ride = new StandardRide();
        FareStrategy strategy = new StandardFareStrategy();
        
        double normalFare = strategy.calculateFare(ride, 10.0, 1.0);
        double surgeFare = strategy.calculateFare(ride, 10.0, 1.5);
        
        assertThat(surgeFare).isGreaterThan(normalFare);
        assertThat(surgeFare).isEqualTo(normalFare * 1.5);
    }
}
