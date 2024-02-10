package model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Эпики")
class EpicTest {
    @Test
    @DisplayName("Проверка равенства по ID")
    public void shouldBeEqualWhenSameId() {
        Epic epic1 = new Epic("Эпик1", "Описание1");
        epic1.setId(1);

        Epic epic2 = new Epic("Эпик2", "Описание2");
        epic2.setId(1);

        assertEquals(epic1, epic2, "Эпики с одинаковым ID должны быть равны!");
    }
}