package ru.practicum.ewm.mainservice.event.repository;

public final class EventTable {
  private EventTable() {
    //utility class
  }

  public static final String ID = "id";
  public static final String ANNOTATION = "annotation";
  public static final String INITIATOR = "initiator";
  public static final String STATE = "state";
  public static final String CATEGORY = "category";
  public static final String EVENT_DATE = "eventDate";
  public static final String LOCATION = "location";
  public static final String VIEWS = "views";
  public static final String PAID = "paid";
  public static final String CONFIRMED_REQUESTS = "confirmedRequests";
  public static final String PARTICIPANT_LIMIT = "participantLimit";
}