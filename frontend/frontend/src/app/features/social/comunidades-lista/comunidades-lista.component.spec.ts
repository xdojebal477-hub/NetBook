import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ComunidadesListaComponent } from './comunidades-lista.component';

describe('ComunidadesListaComponent', () => {
  let component: ComunidadesListaComponent;
  let fixture: ComponentFixture<ComunidadesListaComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ComunidadesListaComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ComunidadesListaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
