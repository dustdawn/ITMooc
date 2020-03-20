package com.njit.media_processor.dao;

import com.njit.framework.domain.media.MediaFile;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author dustdawn
 * @date 2020/3/5 22:08
 */
public interface MediaFileRepository extends MongoRepository<MediaFile,String> {
}
