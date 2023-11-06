package br.com.movie.api.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Getter
@Setter
public class AwardInterval {
    private String producer;
    private Integer interval;
    private Integer previousWin;
    private Integer followingWin;


    public void calculateInterval() {
        if (valid())
            interval = followingWin - previousWin;
    }


    public void set(AwardInterval awardInterval) {
        this.producer = awardInterval.getProducer();
        this.interval = awardInterval.getInterval();
        this.previousWin = awardInterval.getPreviousWin();
        this.followingWin = awardInterval.getFollowingWin();
    }


    public boolean isBiggerThan(AwardInterval awardInterval) {
        if (valid()) {
            if (isNull(awardInterval.getInterval()))
                return true;

            return this.getInterval() > awardInterval.getInterval();
        }
        return false;
    }


    public boolean isSmallerThan(AwardInterval awardInterval) {
        if (valid()) {
            if (isNull(awardInterval.getInterval()))
                return true;

            return this.getInterval() < awardInterval.getInterval();
        }
        return false;
    }


    public boolean isSameSize(AwardInterval awardInterval) {
        if (valid()) {
            if (isNull(awardInterval.getInterval()))
                return false;

            return Objects.equals(this.getInterval(), awardInterval.getInterval());
        }
        return false;
    }


    public boolean isDifferentProducer(AwardInterval awardInterval) {
        return !this.getProducer().equalsIgnoreCase(awardInterval.getProducer());
    }


    public void nextYear(Integer year) {
        setFollowingWin(getPreviousWin());
        setPreviousWin(year);
        calculateInterval();
    }


    private boolean valid() {
        return nonNull(followingWin) && nonNull(previousWin);
    }
}
