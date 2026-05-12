package com.bank.controller;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/data/files")
public class FileController {

    // Upload directory — intentionally uses the raw filename from the request
    private static final String UPLOAD_DIR = "/tmp/uploads/";

    /**
     * Upload a file to the server.
     * <p>
     * VULNERABILITY — Path Traversal (Write):
     * The original filename from the multipart request is used directly without sanitization.
     * Attacker can upload with filename: ../../app/application.yml
     * to overwrite arbitrary files on the filesystem.
     * <p>
     * Example: curl -F "file=@evil.jsp;filename=../../webapps/ROOT/shell.jsp" http://localhost:8082/data/files/upload
     */
    @PostMapping("/upload")
    public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file) throws IOException {
        String filename = file.getOriginalFilename(); // unsanitized — path traversal here

        File uploadDir = new File(UPLOAD_DIR);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        Path destination = Paths.get(UPLOAD_DIR + filename); // traversal: /tmp/uploads/../../etc/cron.d/backdoor
        Files.write(destination, file.getBytes());

        return ResponseEntity.ok("Uploaded: " + destination.toAbsolutePath());
    }

    /**
     * Download / serve a file by name.
     * <p>
     * VULNERABILITY — Path Traversal / Local File Inclusion (LFI):
     * The filename parameter is used directly to read from the filesystem.
     * Attacker can request: GET /data/files/../../etc/passwd
     * or:                   GET /data/files/../../app/application.yml
     * to read arbitrary files.
     * <p>
     * Example: curl http://localhost:8082/data/files/..%2F..%2Fetc%2Fpasswd
     */
    @GetMapping("/{filename:.+}")
    public ResponseEntity<Resource> download(@PathVariable String filename) throws IOException {
        Path filePath = Paths.get(UPLOAD_DIR).resolve(filename); // no normalization, no boundary check
        byte[] content = Files.readAllBytes(filePath);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new ByteArrayResource(content));
    }
}
