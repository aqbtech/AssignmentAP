package com.hcmutap.elearning.dao.impl;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.firebase.cloud.StorageClient;
import com.hcmutap.elearning.model.FileInfo;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Repository
public class FileDAO {
	public void uploadFile(MultipartFile file, FileInfo fileInfo) {
		try {
			String fileName = fileInfo.getFolder() + "/" + fileInfo.getFileName(); // Get the original file name
			Bucket bucket = StorageClient.getInstance().bucket();
			bucket.create(fileName, file.getInputStream(), file.getContentType());
		} catch (IOException e) {
			throw new RuntimeException("Error uploading file", e);
		}
	}

	public ResponseEntity<InputStreamResource> downloadFile(FileInfo fileInfo) {
		try {
			Blob blob = initBucketStorage(fileInfo);
			Path dest = Files.createTempFile("temp", null);
			blob.downloadTo(dest);
			InputStreamResource resource = new InputStreamResource(Files.newInputStream(dest));

			return ResponseEntity.ok()
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileInfo.getFileName())
					.contentLength(Files.size(dest))
					.body(resource);
		} catch (IOException e) {
			throw new RuntimeException("Error downloading file", e);
		}
	}
	public void deleteFile(FileInfo fileInfo) {
		Blob blob = initBucketStorage(fileInfo);
		blob.delete();
	}

	private Blob initBucketStorage(FileInfo fileInfo) {
		String fullPath = (fileInfo.getFolder() == null || fileInfo.getFolder().isEmpty()) ? fileInfo.getFileName() : fileInfo.getFolder() + "/" + fileInfo.getFileName();
		Bucket bucket = StorageClient.getInstance().bucket();
		Blob blob = bucket.get(fullPath);
		if (blob == null) {
			throw new RuntimeException("File not found: " + fullPath);
		}
		return blob;
	}
}
