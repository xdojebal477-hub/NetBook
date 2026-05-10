import { ComponentFixture, TestBed } from '@angular/core/testing';

import { VisorLecturaComponent } from './visor-lectura.component';

describe('VisorLecturaComponent', () => {
  let component: VisorLecturaComponent;
  let fixture: ComponentFixture<VisorLecturaComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [VisorLecturaComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(VisorLecturaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
