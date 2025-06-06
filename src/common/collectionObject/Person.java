package common.collectionObject;

import server.IO.LocalDateAdapter;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;

/**
 * Класс, представляющий персонажа (например, оператора фильма или режиссера).
 * Содержит информацию о человеке, включая имя, дату рождения и номер паспорта.
 */
public class Person implements Serializable {

    private String name; // Поле не может быть null, строка не может быть пустой
    private LocalDate birthday; // Поле не может быть null
    private String passportID; // Длина строки должна быть не меньше 6, поле не может быть null

    /**
     * Устанавливает имя персонажа.
     *
     * @param name name персонажа, не может быть пустым.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Устанавливает дату рождения персонажа.
     *
     * @param birthday Дата рождения персонажа, не может быть null.
     */
    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    /**
     * Устанавливает номер паспорта персонажа.
     *
     * @param passportID Номер паспорта, строка не может быть пустой, длина должна быть не меньше 6 символов.
     */
    public void setPassportID(String passportID) {
        this.passportID = passportID;
    }

    /**
     * Получает имя персонажа.
     *
     * @return name персонажа.
     */
    @XmlElement
    public String getName() {
        return name;
    }

    /**
     * Получает дату рождения персонажа.
     *
     * @return Дата рождения персонажа.
     */
    @XmlElement
    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    public LocalDate getBirthday() {
        return birthday;
    }

    /**
     * Получает номер паспорта персонажа.
     *
     * @return Номер паспорта персонажа.
     */
    @XmlElement
    public String getPassportID() {
        return passportID;
    }
}
