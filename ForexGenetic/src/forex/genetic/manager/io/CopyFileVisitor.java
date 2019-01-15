package forex.genetic.manager.io;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

public class CopyFileVisitor implements FileVisitor<Path> {
	private String sourcePathName, targetPathName, processedPathName;
	private Path sourcePath, targetPath, processedPath;
	private List<Path> copiedFiles;

	public CopyFileVisitor(String sourcePathName, String targetPathName, String processedPathName) {
		super();
		this.sourcePathName = sourcePathName;
		this.targetPathName = targetPathName;
		this.processedPathName = processedPathName;
		this.sourcePath = FileSystems.getDefault().getPath(sourcePathName);
		this.targetPath = FileSystems.getDefault().getPath(targetPathName);
		this.processedPath = FileSystems.getDefault().getPath(processedPathName);
		this.copiedFiles = new ArrayList<>();
	}

	@Override
	public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
		// Path targetdir = target.resolve(source.relativize(dir));
		try {
			Files.copy(dir, processedPath);
		} catch (FileAlreadyExistsException e) {
			if (!Files.isDirectory(processedPath))
				throw e;
		}
		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
		Files.createDirectories(targetPath);
		Path resolvedPath = targetPath.resolve(sourcePath.relativize(file));
		Files.createDirectories(resolvedPath.getParent());
		Files.copy(file, resolvedPath, StandardCopyOption.REPLACE_EXISTING);
		Files.createDirectories(processedPath);
		Path movingResolvedPath = targetPath.resolve(sourcePath.relativize(file));
		Files.createDirectories(movingResolvedPath.getParent());
		Files.move(file, processedPath.resolve(sourcePath.relativize(file)), StandardCopyOption.REPLACE_EXISTING);
		copiedFiles.add(file);
		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
		return FileVisitResult.CONTINUE;
	}

	public List<Path> getCopiedFiles() {
		return copiedFiles;
	}

	public void setCopiedFiles(List<Path> copiedFiles) {
		this.copiedFiles = copiedFiles;
	}

}
