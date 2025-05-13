package org.example.expert.domain.todo.repository;

import java.util.Optional;

import lombok.RequiredArgsConstructor;

import org.example.expert.domain.todo.entity.QTodo;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.user.entity.QUser;
import org.springframework.stereotype.Repository;
import com.querydsl.jpa.impl.JPAQueryFactory;

@Repository
@RequiredArgsConstructor
public class TodoRepositortImple implements TodoRepositoryCustom{
	private JPAQueryFactory jpaQueryFactory;

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
