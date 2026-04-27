package br.com.issufibadji.todolist;

import java.util.ArrayList;
import java.util.List;

import br.com.issufibadji.todolist.entity.Todo;

public class TestConstants {
  public static final List<Todo> TODOS = new ArrayList<>() {
    {
      add(new Todo(9995L, "@issufibadji", "Curtir", false, 1));
      add(new Todo(9996L, "@issufibadji", "Comentar", false, 1));
      add(new Todo(9997L, "@issufibadji", "Compartilhar", false, 1));
      add(new Todo(9998L, "@issufibadji", "Se Inscrever", false, 1));
      add(new Todo(9999L, "@issufibadji", "Ativar as Notificações", false, 1));
    }
  };

  public static final Todo TODO = TODOS.get(0);
}
