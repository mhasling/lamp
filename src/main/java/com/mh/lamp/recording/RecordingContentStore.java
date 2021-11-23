package com.mh.lamp.recording;

import org.springframework.content.commons.repository.ContentStore;
import org.springframework.content.rest.StoreRestResource;

@StoreRestResource
public interface RecordingContentStore extends ContentStore<Recording, byte[]> {
}
