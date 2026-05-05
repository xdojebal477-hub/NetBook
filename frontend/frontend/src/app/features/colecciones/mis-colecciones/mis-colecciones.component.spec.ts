import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MisColeccionesComponent } from './mis-colecciones.component';

describe('MisColeccionesComponent', () => {
  let component: MisColeccionesComponent;
  let fixture: ComponentFixture<MisColeccionesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MisColeccionesComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MisColeccionesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
