package com.example.home_books_javafx_spring.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:fields_text.properties")
public class FieldsConfig {

    public static String DEFAULT_ROOM_NAME;

    public static String DEFAULT_PUBLISHER_NAME;

    public static String DEFAULT_NEW_PUBLISHER_NAME;

    public static String DEFAULT_AUTHOR_FIRST_NAME;

    public static String DEFAULT_AUTHOR_LAST_NAME;

    public static String DEFAULT_NEW_AUTHOR_FIRST_NAME;

    public static String DEFAULT_NEW_AUTHOR_LAST_NAME;

    public static String DEFAULT_STATUS_TYPE;

    public static String DEFAULT_STATUS_TYPE_BORROWED;

    public static String DEFAULT_STATUS_TYPE_HOME;

    @Value("${spring.application.ui.text.default.room.name}")
    private void setDefaultRoomName(String defaultRoomName) {
        DEFAULT_ROOM_NAME = defaultRoomName;
    }

    @Value("${spring.application.ui.text.default.publisher.name}")
    private void setDefaultPublisherName(String defaultPublisherName) {
        DEFAULT_PUBLISHER_NAME = defaultPublisherName;
    }

    @Value("${spring.application.ui.text.default.author.firstname}")
    private void setDefaultAuthorFirstName(String defaultAuthorFirstName) {
        DEFAULT_AUTHOR_FIRST_NAME = defaultAuthorFirstName;
    }

    @Value("${spring.application.ui.text.default.author.lastname}")
    private void setDefaultAuthorLastName(String defaultAuthorLastName) {
        DEFAULT_AUTHOR_LAST_NAME = defaultAuthorLastName;
    }

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

    @Value("${spring.application.ui.text.default.new_publisher.name}")
    public void setDefaultNewPublisherName(String defaultNewPublisherName) {
        DEFAULT_NEW_PUBLISHER_NAME = defaultNewPublisherName;
    }

    @Value("${spring.application.ui.text.default.new_author.firstname}")
    public void setDefaultNewAuthorFirstName(String defaultNewAuthorFirstName) {
        DEFAULT_NEW_AUTHOR_FIRST_NAME = defaultNewAuthorFirstName;
    }

    @Value("${spring.application.ui.text.default.new_author.lastname}")
    public void setDefaultNewAuthorLastName(String defaultNewAuthorLastName) {
        DEFAULT_NEW_AUTHOR_LAST_NAME = defaultNewAuthorLastName;
    }
}
