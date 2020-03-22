package com.njit.filesystem.dao;

import com.njit.framework.domain.filesystem.FileSystem;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author dustdawn
 * @date 2019/12/11 17:41
 */
public interface FileSystemRepository extends MongoRepository<FileSystem, String> {
}
