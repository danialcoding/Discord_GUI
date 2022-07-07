package project.Discord.server.entity;

import java.io.Serializable;

public enum Status implements Serializable {
    NULL,
    OFFLINE,
    ONLINE,
    IDLE,
    DO_NOT_DISTURB,
    INVISIBLE
}