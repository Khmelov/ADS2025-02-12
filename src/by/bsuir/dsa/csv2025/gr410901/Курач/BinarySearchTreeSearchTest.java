package by.bsuir.dsa.csv2025.gr410901.Курач;

import org.junit.Before;
import org.junit.Test;

import static by.bsuir.dsa.csv2025.gr410901.Курач.BinarySearchTreeSearch.buildTestTree;
import static by.bsuir.dsa.csv2025.gr410901.Курач.BinarySearchTreeSearch.find;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

// Тесты с использованием JUnit
    public class BinarySearchTreeSearchTest {
        private Solution root;
        
        @Before
        public void setUp() {
            root = buildTestTree();
        }
        
        @Test
        public void testFindExistingElement() {
            // Тест 1: Поиск существующего элемента (6)
            assertTrue("Should find existing element 6", find(root, 6));
        }
        
        @Test
        public void testFindNonExistingElement() {
            // Тест 2: Поиск несуществующего элемента (5)
            assertFalse("Should not find non-existing element 5", find(root, 5));
        }
        
        @Test
        public void testFindRootElement() {
            // Тест 3: Поиск корневого элемента (8)
            assertTrue("Should find root element 8", find(root, 8));
        }
        
        @Test
        public void testFindLeafElement() {
            // Тест 4: Поиск листового элемента (13)
            assertTrue("Should find leaf element 13", find(root, 13));
        }
        
        @Test
        public void testFindInEmptyTree() {
            // Тест 5: Поиск в пустом дереве (5)
            Solution emptyTree = null;
            assertFalse("Should return false for empty tree", find(emptyTree, 5));
        }
        
        @Test
        public void testFindMiddleLeftSubtree() {
            // Тест 6: Поиск среднего элемента в левом поддереве (3)
            assertTrue("Should find element 3 in left subtree", find(root, 3));
        }
        
        @Test
        public void testFindMiddleRightSubtree() {
            // Тест 7: Поиск среднего элемента в правом поддереве (10)
            assertTrue("Should find element 10 in right subtree", find(root, 10));
        }
        
        @Test
        public void testFindElementWithOneChild() {
            // Тест 8: Поиск элемента с одним потомком (14)
            assertTrue("Should find element 14 with one child", find(root, 14));
        }
        
        @Test
        public void testFindNonExistingBetweenExisting() {
            // Тест 9: Поиск несуществующего элемента между существующими (2)
            assertFalse("Should not find non-existing element 2", find(root, 2));
        }
        
        @Test
        public void testFindNonExistingGreaterThanMax() {
            // Тест 10: Поиск несуществующего элемента больше максимального (20)
            assertFalse("Should not find element greater than max", find(root, 20));
        }
        
        @Test
        public void testFindNonExistingLessThanMin() {
            // Тест 11: Поиск несуществующего элемента меньше минимального (0)
            assertFalse("Should not find element less than min", find(root, 0));
        }
        
        @Test
        public void testFindAllExistingElements() {
            // Дополнительный тест: проверка всех существующих элементов
            int[] existingElements = {1, 3, 4, 6, 7, 8, 10, 13, 14};
            for (int element : existingElements) {
                assertTrue("Should find existing element: " + element, find(root, element));
            }
        }
        
        @Test
        public void testFindNonExistingEdgeCases() {
            // Дополнительный тест: проверка граничных несуществующих элементов
            int[] nonExistingElements = {2, 5, 9, 11, 12, 15, 20, 0, -1};
            for (int element : nonExistingElements) {
                assertFalse("Should not find non-existing element: " + element, find(root, element));
            }
        }
    }