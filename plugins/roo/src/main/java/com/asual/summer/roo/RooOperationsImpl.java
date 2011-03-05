package com.asual.summer.roo;

import java.io.File;
import java.io.IOException;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.springframework.roo.process.manager.FileManager;
import org.springframework.roo.process.manager.MutableFile;
import org.springframework.roo.project.Path;
import org.springframework.roo.project.PathResolver;
import org.springframework.roo.project.ProjectOperations;
import org.springframework.roo.support.util.Assert;
import org.springframework.roo.support.util.FileCopyUtils;
import org.springframework.roo.support.util.TemplateUtils;

/**
 * Implementation of {@link RooOperations} interface.
 *
 * @since 1.1.1
 */
@Component
@Service
public class RooOperationsImpl implements RooOperations {
	private static final char SEPARATOR = File.separatorChar;

	/**
	 * Get a reference to the FileManager from the underlying OSGi container. Make sure you
	 * are referencing the Roo bundle which contains this service in your add-on pom.xml.
	 * 
	 * Using the Roo file manager instead if java.io.File gives you automatic rollback in case
	 * an Exception is thrown.
	 */
	@Reference private FileManager fileManager;
	
	/**
	 * Get a reference to the ProjectOperations from the underlying OSGi container. Make sure you
	 * are referencing the Roo bundle which contains this service in your add-on pom.xml.
	 */
	@Reference private ProjectOperations projectOperations;

	 /** {@inheritDoc} */
	public boolean isInstallTagsCommandAvailable() {
		return projectOperations.isProjectAvailable() && fileManager.exists(projectOperations.getProjectMetadata().getPathResolver().getIdentifier(Path.SRC_MAIN_WEBAPP, "WEB-INF" + SEPARATOR + "tags"));
	}

	 /** {@inheritDoc} */
	public String getProperty(String propertyName) {
		Assert.hasText(propertyName, "Property name required");
		return System.getProperty(propertyName);
	}

	 /** {@inheritDoc} */
	public void installTags() {
		// Use PathResolver to get canonical resource names for a given artifact
		PathResolver pathResolver = projectOperations.getProjectMetadata().getPathResolver();
		createOrReplaceFile(pathResolver.getIdentifier(Path.SRC_MAIN_WEBAPP, "WEB-INF" + SEPARATOR + "tags" + SEPARATOR + "util"), "info.tagx");
		createOrReplaceFile(pathResolver.getIdentifier(Path.SRC_MAIN_WEBAPP, "WEB-INF" + SEPARATOR + "tags" + SEPARATOR + "form"), "show.tagx");
	}
	
	/**
	 * A private method which illustrates how to reference and manipulate resources
	 * in the target project as well as the bundle classpath.
	 * 
	 * @param path
	 * @param fileName
	 */
	private void createOrReplaceFile(String path, String fileName) {
		String targetFile = path + SEPARATOR + fileName;
		
		// Use MutableFile in combination with FileManager to take advantage of Roo's transactional file handling which 
		// offers automatic rollback if an exception occurs
		MutableFile mutableFile = fileManager.exists(targetFile) ? fileManager.updateFile(targetFile) : fileManager.createFile(targetFile);
		try {
			// Use FileCopyUtils for copying resources from your add-on to the target project
			// Use TemplateUtils to open an InputStream to a resource located in your bundle
			FileCopyUtils.copy(TemplateUtils.getTemplate(getClass(), fileName), mutableFile.getOutputStream());
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}
}