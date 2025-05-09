package org.example.expert.domain.common.exception;

//lv1-4 테스트코드 퀴즈-컨트롤 테스트 이해
public class NotFoundException extends RuntimeException {
	public NotFoundException(String message) {
		super(message);
	}
}
