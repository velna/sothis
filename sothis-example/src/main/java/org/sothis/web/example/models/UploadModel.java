package org.sothis.web.example.models;

import java.io.File;
import java.io.InputStream;

public class UploadModel extends BaseModel {

	private static final long serialVersionUID = 3536681367255750346L;

	private File image;
	private String imageFileName;
	private String imageContentType;
	private InputStream imageInputStream;

	public File getImage() {
		return image;
	}

	public void setImage(File image) {
		this.image = image;
	}

	public String getImageFileName() {
		return imageFileName;
	}

	public void setImageFileName(String imageFileName) {
		this.imageFileName = imageFileName;
	}

	public String getImageContentType() {
		return imageContentType;
	}

	public void setImageContentType(String imageContentType) {
		this.imageContentType = imageContentType;
	}

	public InputStream getImageInputStream() {
		return imageInputStream;
	}

	public void setImageInputStream(InputStream imageInputStream) {
		this.imageInputStream = imageInputStream;
	}

}
