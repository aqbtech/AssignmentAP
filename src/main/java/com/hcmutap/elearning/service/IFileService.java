package com.hcmutap.elearning.service;

import com.hcmutap.elearning.model.FileInfo;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IFileService {
	void uploadFile(MultipartFile file, FileInfo fileInfo);

	ResponseEntity<InputStreamResource> downloadFile(FileInfo fileInfo);
	void deleteFile(FileInfo fileInfo);
	List<FileInfo> listFiles(String folder);
}
