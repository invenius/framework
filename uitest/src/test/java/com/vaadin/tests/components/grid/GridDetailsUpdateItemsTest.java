package com.vaadin.tests.components.grid;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.number.IsCloseTo.closeTo;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;
import org.openqa.selenium.By;

import com.vaadin.testbench.TestBenchElement;
import com.vaadin.testbench.elements.ButtonElement;
import com.vaadin.testbench.elements.GridElement;
import com.vaadin.testbench.elements.GridElement.GridCellElement;
import com.vaadin.tests.tb3.MultiBrowserTest;

public class GridDetailsUpdateItemsTest extends MultiBrowserTest {

    @Test
    public void testDetailsUpdateWithItems() {
        openTestURL();
        GridElement grid = $(GridElement.class).first();
        ButtonElement button = $(ButtonElement.class)
                .caption("Change with details").first();

        String details0 = grid.getDetails(0).getText();

        // change the contents
        button.click();
        sleep(200);

        TestBenchElement detailCell0 = grid.getDetails(0);
        // ensure contents have updated
        String updatedDetails0 = detailCell0.getText();
        assertNotEquals("Details should not be empty", "", updatedDetails0);
        assertNotEquals("Unexpected detail contents for row 0", details0,
                updatedDetails0);

        GridCellElement cell1_0 = grid.getCell(1, 0);
        TestBenchElement detailCell1 = grid.getDetails(1);

        // ensure positioning is correct
        assertDirectlyAbove(detailCell0, cell1_0);
        assertDirectlyAbove(cell1_0, detailCell1);
    }

    @Test
    public void testRemovingDetailsWithItemUpdateRepositionsRowsCorrectly() {
        openTestURL();
        GridElement grid = $(GridElement.class).first();
        ButtonElement button = $(ButtonElement.class)
                .caption("Change without details").first();

        assertFalse("Details not found when there should be some.",
                grid.findElements(By.className("v-grid-spacer")).isEmpty());

        // change the contents
        button.click();

        waitForElementNotPresent(By.className("v-grid-spacer"));

        GridCellElement cell0_0 = grid.getCell(0, 0);
        GridCellElement cell1_0 = grid.getCell(1, 0);

        // ensure positioning is correct
        assertDirectlyAbove(cell0_0, cell1_0);
    }

    private void assertDirectlyAbove(TestBenchElement above,
            TestBenchElement below) {
        int aboveBottom = above.getLocation().getY()
                + above.getSize().getHeight();
        int belowTop = below.getLocation().getY();
        assertThat("Unexpected positions.", (double) aboveBottom,
                closeTo(belowTop, 1d));
    }
}
