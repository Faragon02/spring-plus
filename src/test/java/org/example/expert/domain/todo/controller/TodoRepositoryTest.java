package org.example.expert.domain.todo.controller;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.example.expert.domain.common.exception.InvalidRequestException;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.todo.repository.TodoRepository;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.enums.UserRole;
import org.example.expert.domain.user.repository.UserRepository;
import org.example.expert.util.DataUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest
public class TodoRepositoryTest {
	@Autowired
	TodoRepository todoRepository;
	@Autowired
	private UserRepository userRepository;
	private DataUtil DateUtil;


	@Transactional
	@Rollback(false)
	@Test
	void 과제_쿼리_테스트() {
        //기본 데이터 추가

		//데이터 추가
		User user  = new User("파라곤", "wpsptltm888@gamil.com", "tester1234", UserRole.USER);
		userRepository.save(user);
		userRepository.flush();
		// User user = userRepository.findById(7L).orElseThrow(() -> new InvalidRequestException("User not found"));
		//random Data 생성하기
		String[] titles = {"Do homework", "Study", "Workout", "Clean", "Go to market", "Read", "Code", "Play game",
			"Walk", "Rest"};
		String[] contents = {"Task A", "Task B", "Task C", "Task D"};
		String[] weathers = {"SUNNY", "CLOUDY", "RAINY", "SNOWY", null};
		Random randomContentsSelect = new Random();
		Random randomWeathersSelect = new Random();
		List<Todo> newTodos = new ArrayList<>();
		for (int i = 0; i < titles.length; i++) {
			int randomContentsSelectNum =randomContentsSelect.nextInt(contents.length);
			int randomWeathersSelectNum = randomWeathersSelect.nextInt(weathers.length );


			newTodos.add(new Todo(titles[i], contents[randomContentsSelectNum], weathers[randomWeathersSelectNum],
				user));

		}
		todoRepository.saveAll(newTodos);
		todoRepository.flush();
		for(int j =0; j< titles.length; j++ ){
			LocalDateTime randomTime= DateUtil.getRandomDateTimeWithinDays(4);
			todoRepository.updateModifiedAt(newTodos.get(j).getId(),randomTime);
		}
		todoRepository.flush();

		//given
		Pageable pageable = PageRequest.of(0, 10);
		String weather = weathers[0]; // 또는 null 테스트도 가능
		// LocalDateTime start = LocalDateTime.now().minusDays(3);
		// LocalDateTime end = LocalDateTime.now();
		LocalDateTime start = LocalDateTime.of(2025,05,06,0,0);
		LocalDateTime end = LocalDateTime.of(2025,05,9,11,59);
		// LocalDateTime start = null;
		// LocalDateTime end = null;

		//when
		Page<Todo> todos  = todoRepository.searchTodo(weather,start,end,pageable);

		//then
		assertThat(todos).isNotNull();
		assertThat(todos.getContent().size()).isLessThanOrEqualTo(10);
		// todos.forEach(todo -> System.out.printf(
		// 	"제목: %s | 내용: %s | 날씨: %s | 수정일: %s | 작성자: %s%n",
		// 	todo.getTitle(),
		// 	todo.getContents(),
		// 	todo.getWeather(),
		// 	todo.getModifiedAt(),
		// 	todo.getUser().getNickName() // 또는 getUsername 등 User 객체에 맞게
		// ));
	}
}
