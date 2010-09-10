package com.asual.summer.sample.convert;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.asual.summer.sample.domain.Image;

@Component
public class MultipartFileToImageConvertor implements Converter<MultipartFile, Image> {

	@Override
	public Image convert(MultipartFile source) {
		try {
			return new Image(source);
		} catch (Exception e) {
			return null;
		}
	}

}