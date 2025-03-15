package com.egov.tendering.bidding.service;


import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {

    /**
     * Stores a file with a specified prefix and returns the generated filename
     *
     * @param file the file to store
     * @param prefix the prefix to use for the stored file name
     * @return the generated filename for the stored file
     * @throws com.egov.tendering.bidding.exception.FileStorageException if an error occurs during storage
     */
    String storeFile(MultipartFile file, String prefix);

    /**
     * Loads a file as a Resource that can be served for download
     *
     * @param fileName the name of the file to load
     * @return a Resource representing the file
     * @throws com.egov.tendering.bidding.exception.FileStorageException if the file cannot be found or loaded
     */
    Resource loadFileAsResource(String fileName);

    /**
     * Deletes a file from storage
     *
     * @param fileName the name of the file to delete
     * @return true if the file was successfully deleted, false otherwise
     */
    boolean deleteFile(String fileName);
}