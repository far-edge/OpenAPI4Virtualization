package ch.supsi.isteps.fileserverspring.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import ch.supsi.isteps.fileserverspring.configuration.ConfigurationData;

@Controller
@EnableAutoConfiguration
@SpringBootApplication
public class FileServerController {

	public static void main(String[] args) {
		System.getProperties().put("server.port", ConfigurationData.FILE_SERVER_PORT);
		SpringApplication.run(FileServerController.class, args);
	}

	@RequestMapping(value = "/download/{fileName:.+}", method = RequestMethod.GET)
	public @ResponseBody void download(@PathVariable("fileName") String aFileName, HttpServletResponse response) throws IOException {
		System.out.println("Retrieving file: " + aFileName);
		//File file = getFile(aFileName + ".jar");
		File file = getFile(aFileName);
		InputStream in = new FileInputStream(file);
		response.setHeader("Content-Disposition", "attachment; filename=" + file.getName());
		response.setHeader("Content-Length", String.valueOf(file.length()));
		FileCopyUtils.copy(in, response.getOutputStream());
	}

	@RestController
	@RequestMapping("/upload")
	public class FileUploadController {
		@RequestMapping(method = RequestMethod.POST)
		public String handleFileUpload(@RequestParam("user-file") MultipartFile multipartFile) throws IOException {
			String fileName = multipartFile.getOriginalFilename();
			byte[] bytes = multipartFile.getBytes();
			FileUtils.writeByteArrayToFile(new File(ConfigurationData.FILE_PATH + "/"+ fileName +".jar"), bytes);
			return ConfigurationData.FILE_SERVER_URL + "/download/"+ fileName + ".jar";
		}
	}

	private File getFile(String aFileName) throws FileNotFoundException {
		System.out.println("getFile : " + aFileName);
		String filePath = ConfigurationData.FILE_PATH + "/"+ aFileName;
		File file = new File(filePath);
		if (!file.exists()) {
			throw new FileNotFoundException("file with path: " + filePath + " was not found.");
		}
		return file;
	}
}