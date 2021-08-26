package com.example.home_books_javafx_spring.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.ArrayList;
import java.util.List;

@Configuration
@PropertySource("classpath:fields_text.properties")
public class FieldsConfig {

    public static List<String> STRING_FOR_CHOOSING = new ArrayList<>();

    /* ROOM */
    public static String DEFAULT_ROOM_NAME;

    public static String DEFAULT_CHOOSE_ROOM_NAME;

    /* PUBLISHER */
    public static String DEFAULT_PUBLISHER_NAME;

    public static String DEFAULT_CHOOSE_PUBLISHER_NAME;

    public static String DEFAULT_NEW_PUBLISHER_NAME;

    /* AUTHOR */
    public static String DEFAULT_AUTHOR_NAME;

    public static String DEFAULT_NEW_AUTHOR_NAME;

    public static String DEFAULT_CHOOSE_AUTHOR_NAME;

    /* STATUS TYPE */
    public static String DEFAULT_STATUS_TYPE;

    public static String DEFAULT_CHOOSE_STATUS_TYPE;

    public static String DEFAULT_STATUS_TYPE_BORROWED;

    public static String DEFAULT_STATUS_TYPE_HOME;

    /* ROOM */
    @Value("${spring.application.ui.text.default.room.name}")
    private void setDefaultRoomName(String defaultRoomName) {
        DEFAULT_ROOM_NAME = defaultRoomName;
    }

    @Value("${spring.application.ui.text.default.room.choose.name}")
    public void setDefaultChooseRoomName(String defaultChooseRoomName) {
        DEFAULT_CHOOSE_ROOM_NAME = defaultChooseRoomName;
        STRING_FOR_CHOOSING.add(defaultChooseRoomName);
    }

    /* PUBLISHER */
    @Value("${spring.application.ui.text.default.publisher.name}")
    private void setDefaultPublisherName(String defaultPublisherName) {
        DEFAULT_PUBLISHER_NAME = defaultPublisherName;
    }

    @Value("${spring.application.ui.text.default.new_publisher.name}")
    public void setDefaultNewPublisherName(String defaultNewPublisherName) {
        DEFAULT_NEW_PUBLISHER_NAME = defaultNewPublisherName;
    }

    @Value("${spring.application.ui.text.default.publisher.choose.name}")
    public void setDefaultChoosePublisherName(String defaultChoosePublisherName) {
        DEFAULT_CHOOSE_PUBLISHER_NAME = defaultChoosePublisherName;
        STRING_FOR_CHOOSING.add(defaultChoosePublisherName);
    }

    /* AUTHOR */
    @Value("${spring.application.ui.text.default.author.name}")
    private void setDefaultAuthorFirstName(String defaultAuthorName) {
        DEFAULT_AUTHOR_NAME = defaultAuthorName;
    }

    @Value("${spring.application.ui.text.default.new_author.name}")
    public void setDefaultNewAuthorName(String defaultNewAuthorName) {
        DEFAULT_NEW_AUTHOR_NAME = defaultNewAuthorName;
    }

    @Value("${spring.application.ui.text.default.new_author.choose.name}")
    public void setDefaultChooseAuthorName(String defaultChooseAuthorName) {
        DEFAULT_CHOOSE_AUTHOR_NAME = defaultChooseAuthorName;
        STRING_FOR_CHOOSING.add(defaultChooseAuthorName);
    }

    /* STATUS TYPE */
    @Value("${spring.application.ui.text.default.status_type.no_status}")
    private void setDefaultStatusType(String defaultStatusType) {
        DEFAULT_STATUS_TYPE = defaultStatusType;
    }

    @Value("${spring.application.ui.text.default.status_type.borrowed}")
    private void setDefaultStatusTypeBorrowed(String defaultStatusTypeBorrowed) {
        DEFAULT_STATUS_TYPE_BORROWED = defaultStatusTypeBorrowed;
    }

    @Value("${spring.application.ui.text.default.status_type.home}")
    private void setDefaultStatusTypeHome(String defaultStatusTypeHome) {
        DEFAULT_STATUS_TYPE_HOME = defaultStatusTypeHome;
    }

    @Value("${spring.application.ui.text.default.status_type.choose_status}")
    public void setDefaultChooseStatusType(String defaultChooseStatusType) {
        DEFAULT_CHOOSE_STATUS_TYPE = defaultChooseStatusType;
    }
}
