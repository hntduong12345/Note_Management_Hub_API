package com.notemanagement.NoteManagementHubAPI.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;

public abstract class BaseController {
    protected final Logger logger = LoggerFactory.getLogger(getClass());
}
