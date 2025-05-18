import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ChartExpensesComponent } from './chart-expenses.component';

describe('ChartExpensesComponent', () => {
  let component: ChartExpensesComponent;
  let fixture: ComponentFixture<ChartExpensesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ChartExpensesComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ChartExpensesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
