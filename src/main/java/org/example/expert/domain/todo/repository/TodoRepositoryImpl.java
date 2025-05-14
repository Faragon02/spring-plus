package org.example.expert.domain.todo.repository;

import java.util.Optional;

import lombok.RequiredArgsConstructor;

import org.example.expert.domain.todo.entity.QTodo;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.user.entity.QUser;
import com.querydsl.jpa.impl.JPAQueryFactory;

@RequiredArgsConstructor
public class TodoRepositoryImpl implements TodoRepositoryCustom{

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public Optional<Todo> findByIdWithUser(Long todoId) {
		QTodo todo= QTodo.todo;
		QUser user = QUser.user;

		return Optional.ofNullable(
			jpaQueryFactory
				.selectFrom(todo)
				.join(todo.user, user)
				.fetchJoin()
				.where(todo.id.eq(todoId))
				.fetchOne()
		);
	}
}
