package edsh.mainclasses;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.NoSuchElementException;

import edsh.helpers.ConsolePrinter;
import edsh.helpers.Printer;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.json.JSONException;
import org.json.JSONObject;

import edsh.enums.EventType;
import edsh.exeptions.WrongFieldException;
import edsh.helpers.MyScanner;

@EqualsAndHashCode
public class Event implements Comparable<Event>, Serializable {
	@EqualsAndHashCode.Exclude
    private Long id; //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private final String name; //Поле не может быть null, Строка не может быть пустой
    private final LocalDate date; //Поле может быть null
    private final long minAge;
    private final long ticketsCount; //Значение поля должно быть больше 0
    private final EventType eventType; //Поле не может быть null
    
    private static long lastId = 0;
	@Getter
	private static final MainclassFactory<Event> factory = new EventFactory();
    
    public Event(String name, LocalDate date, long minAge, long ticketsCount, EventType eventType) throws WrongFieldException {
    	id = ++lastId;
    	
    	if(name == null || name.isEmpty() || ticketsCount <= 0 || eventType == null)
			throw new WrongFieldException("Недопустимое значение поля");
    	
    	this.name = name;
    	this.date = date;
    	this.minAge = minAge;
    	this.ticketsCount = ticketsCount;
    	this.eventType = eventType;
	}
    
    public Event(JSONObject jObj) throws WrongFieldException {
		try {
			this.id = jObj.getLong("id");
	    	this.name = jObj.getString("name");
			this.date = LocalDate.parse(jObj.getString("date"));
			this.minAge = jObj.getLong("minAge");
			this.ticketsCount = jObj.getLong("ticketsCount");
			this.eventType = EventType.valueOf(jObj.getString("eventType"));
		} catch (JSONException e) {
			throw new WrongFieldException("Ошибка в получении поля");
		}
		lastId = Math.max(lastId, this.id);
    }
    
    /**
     * Метод собирает {@link JSONObject} по своему объекту
     * @return Все поля объекта в виде {@link JSONObject}
     */
    public JSONObject toJsonObject() {
    	JSONObject jObj = new JSONObject();
    	jObj.put("id", id).put("name", name).put("date", date.toString()).put("minAge", minAge)
    		.put("ticketsCount", ticketsCount).put("eventType", eventType.name());
    	return jObj;
    }
    
    @Override
    public String toString() {
    	String out = "Событие #" + id + ":\n" +
    			"   - Имя: " + name + "\n" +
    			"   - Дата проведения: " + date + "\n" +
    			"   - Минимальный возраст: " + minAge + "\n" +
    			"   - Количество билетов: " + ticketsCount + "\n" +
    			"   - Тип: " + eventType;
    	return out;
    }
    
    @Override
	public int compareTo(Event ev) {
		return this.date.compareTo(ev.date);
	}
    
}