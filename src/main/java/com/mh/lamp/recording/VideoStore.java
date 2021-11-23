package com.mh.lamp.recording;

import org.springframework.content.commons.repository.Store;
import org.springframework.content.rest.StoreRestResource;

@StoreRestResource(path = "/videos")
public interface VideoStore extends Store<String> {
}