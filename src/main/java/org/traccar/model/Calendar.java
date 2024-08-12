
package org.traccar.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.filter.Filter;
import net.fortuna.ical4j.filter.predicate.PeriodRule;
import net.fortuna.ical4j.model.DateTime;
import net.fortuna.ical4j.model.Period;
import net.fortuna.ical4j.model.component.CalendarComponent;
import net.fortuna.ical4j.model.component.VEvent;
import org.traccar.storage.QueryIgnore;
import org.traccar.storage.StorageName;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@StorageName("tc_calendars")
public class Calendar extends ExtendedModel {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private byte[] data;

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) throws IOException, ParserException {
        CalendarBuilder builder = new CalendarBuilder();
        calendar = builder.build(new ByteArrayInputStream(data));
        this.data = data;
    }

    private net.fortuna.ical4j.model.Calendar calendar;

    @QueryIgnore
    @JsonIgnore
    public net.fortuna.ical4j.model.Calendar getCalendar() {
        return calendar;
    }

    private Collection<VEvent> findEvents(Date date) {
        if (calendar != null) {
            var filter = new Filter<VEvent>(new PeriodRule<>(new Period(new DateTime(date), Duration.ZERO)));
            return filter.filter(calendar.getComponents(CalendarComponent.VEVENT));
        } else {
            return List.of();
        }
    }

    public Set<Period> findPeriods(Date date) {
        return findEvents(date).stream()
                .flatMap((e) -> e.calculateRecurrenceSet(new Period(new DateTime(date), Duration.ZERO)).stream())
                .collect(Collectors.toSet());
    }

    public boolean checkMoment(Date date) {
        return !findEvents(date).isEmpty();
    }

}
