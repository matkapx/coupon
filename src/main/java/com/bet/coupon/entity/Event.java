package com.bet.coupon.entity;

import com.bet.coupon.enums.EventType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDateTime;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table
public class Event extends BaseEntity {

  @Id
  @SequenceGenerator(name = "GEN_EVENT", sequenceName = "SEQ_EVENT", allocationSize = 1)
  @GeneratedValue(generator = "GEN_EVENT", strategy = GenerationType.SEQUENCE)
  @Column(name = "ID", nullable = false)
  private Long id;
  @Column(name = "NAME")
  private String name;
  @Column(name = "MBS")
  private int mbs;
  @Column(name = "EVENT_TYPE")
  @Enumerated(EnumType.ORDINAL)
  private EventType type;
  @Column(name = "EVENT_DATE")
  private LocalDateTime eventDate;
  @JsonIgnore
  @OneToMany(mappedBy = "event", fetch = FetchType.LAZY)
  Set<CouponSelection> events;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getMbs() {
    return mbs;
  }

  public void setMbs(int mbs) {
    this.mbs = mbs;
  }

  public EventType getType() {
    return type;
  }

  public void setType(EventType type) {
    this.type = type;
  }

  public LocalDateTime getEventDate() {
    return eventDate;
  }

  public void setEventDate(LocalDateTime eventDate) {
    this.eventDate = eventDate;
  }

  public Set<CouponSelection> getEvents() {
    return events;
  }

  public void setEvents(Set<CouponSelection> events) {
    this.events = events;
  }
}
