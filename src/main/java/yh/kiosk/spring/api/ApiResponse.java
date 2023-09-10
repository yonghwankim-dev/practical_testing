package yh.kiosk.spring.api;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class ApiResponse<T> {

	private int code;

	private HttpStatus status;

	private String message;

	private T data;

	private ApiResponse() {

	}

	public ApiResponse(HttpStatus status, String message, T data) {
		this.code = status.value();
		this.status = status;
		this.message = message;
		this.data = data;
	}

	public static <T> ApiResponse<T> of(HttpStatus status, T data) {
		return of(status, status.name(), data);
	}

	public static <T> ApiResponse<T> of(HttpStatus status, String message, T data) {
		return new ApiResponse<>(status, message, data);
	}

	public static <T> ApiResponse<T> ok(T data) {
		return new ApiResponse<>(HttpStatus.OK, HttpStatus.OK.name(), data);
	}
}
