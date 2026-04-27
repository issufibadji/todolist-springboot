package br.com.issufibadji.todolist.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.issufibadji.todolist.entity.Todo;

public interface TodoRepository extends JpaRepository<Todo, Long> {

}
