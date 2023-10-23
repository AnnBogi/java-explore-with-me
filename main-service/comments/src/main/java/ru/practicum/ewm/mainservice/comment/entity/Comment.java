package ru.practicum.ewm.mainservice.comment.entity;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.ewm.mainservice.event.entity.Event;
import ru.practicum.ewm.mainservice.user.entity.User;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.time.LocalDateTime;

@Entity
@Table(name = "comments", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"author_id", "event_id", "comment"})
})
@Getter
@Setter
public class Comment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "author_id", nullable = false)
  private User author;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "event_id", nullable = false)
  private Event event;

  @Column(length = 2048, nullable = false)
  private String comment;

  @Column(nullable = false)
  private LocalDateTime created;

  private LocalDateTime edited;

  @Column(name = "is_initiator")
  private boolean isInitiator;
}