package com.hcmutap.elearning.service.impl;

import com.hcmutap.elearning.dao.impl.FileDAO;
import com.hcmutap.elearning.model.FileInfo;
import com.hcmutap.elearning.service.IFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class FileService implements IFileService {
	private final FileDAO fileDAO;
	@Autowired
	public FileService(FileDAO fileDAO) {
		this.fileDAO = fileDAO;
	}
	@Override
	public void uploadFile(MultipartFile file, FileInfo fileInfo) {
		fileDAO.uploadFile(file, fileInfo);
	}

	@Override
	public ResponseEntity<InputStreamResource> downloadFile(FileInfo fileInfo) {
		return fileDAO.downloadFile(fileInfo);
	}
	@Override
	public void deleteFile(FileInfo fileInfo) {
		fileDAO.deleteFile(fileInfo);
	}

	@Override
	public List<FileInfo> listFiles(String folder) {
		return null;
	}
}
