package com.ieshermanosmachado.netbook.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FileStorageService {
    
    private final Path fileStorageLocation;

    public FileStorageService() {
        // Carpeta donde se guardarán físicamente los archivos dentro del servidor
        this.fileStorageLocation = Paths.get("uploads/libros").toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
            log.info("[FileStorageService] Directorio asegurado: {}", this.fileStorageLocation);
        } catch (Exception ex) {
            log.error("[FileStorageService] Error creando directorio para almacenar en: {}", this.fileStorageLocation, ex);
            throw new RuntimeException("No se pudo crear el directorio para almacenar los libros.", ex);
        }
    }

    public String guardarArchivo(MultipartFile archivo) {
        log.info("[FileStorageService] Guardando archivo... Original: {}", archivo.getOriginalFilename());
        if (archivo.isEmpty()) {
            log.error("[FileStorageService] Intento de guardado con archivo vacío.");
            throw new RuntimeException("El archivo está vacío.");
        }

        //validacion de formato (Solo PDF, TXT, PNG, JPG, JPEG, WEBP) - RF-03
        String contentType = archivo.getContentType();
        if (contentType == null) {
            log.error("[FileStorageService] No se detectó Content-Type en el archivo.");
            throw new RuntimeException("No se pudo detectar el tipo de archivo.");
        }

        String extension;
        if (contentType.equals("application/pdf")) {
            extension = ".pdf";
        } else if (contentType.equals("text/plain")) {
            extension = ".txt";
        } else if (contentType.equals("image/png")) {
            extension = ".png";
        } else if (contentType.equals("image/jpeg") || contentType.equals("image/jpg")) {
            extension = ".jpg";
        } else if (contentType.equals("image/webp")) {
            extension = ".webp";
        } else {
            throw new RuntimeException("Formato no válido. (Archivos permitidos: PDF, TXT, PNG, JPG, WEBP).");
        }

        // Generamos un nombre mediante el uso de UUID para evitar que dos archivos se sobrescriban
        String newFileName = UUID.randomUUID().toString() + extension;

        try {
            Path targetLocation = this.fileStorageLocation.resolve(newFileName);
            Files.copy(archivo.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            
            // Devolvemos el nombre del archivo  (archivo_url)
            return newFileName;
        } catch (IOException ex) {
            throw new RuntimeException("Fallo al almacenar el archivo en disco.", ex);
        }
    }
    public Resource cargarArchivoComoRecurso(String nombreArchivo) {
        try {
            Path filePath = this.fileStorageLocation.resolve(nombreArchivo).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Archivo no encontrado o no se puede leer: " + nombreArchivo);
            }
        } catch (Exception ex) {
            throw new RuntimeException("Archivo no encontrado: " + nombreArchivo, ex);
        }
    }

}