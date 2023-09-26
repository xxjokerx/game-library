import {Component, OnInit} from '@angular/core';
import {GameService} from '../../game.service';
import {ActivatedRoute, Router} from '@angular/router';
import {Game} from '../../../../model/game.model';
import {ImageService} from '../../../../shared/services/image.service';
import {FileService} from '../../../../shared/services/file.service';

@Component({
  selector: 'app-image-handler',
  templateUrl: './image-handler.component.html',
  styleUrls: ['./image-handler.component.css']
})
export class ImageHandlerComponent implements OnInit {

  game: Game;
  file: File;

  constructor(private service: ImageService,
              private gameService: GameService,
              private fileService: FileService,
              private route: ActivatedRoute,
              private router: Router) {
  }

  ngOnInit(): void {
    this.game = this.gameService.getDetailedGame();

  }

  onUpload(): void {
    console.log(this.file);
    this.service.uploadImage(this.file, this.game.id).subscribe(
      success => console.log(success),
      err => console.log(err),
    );

  }

  onDelete(): void {
  }

  onBack(): void {

  }

  onFileSelected(event: any): void {
    this.file = event.target.files[0];
  }

  returnFileSize(bytes: number): string {
    if (bytes < 1024) {
      return `${bytes} bytes`;
    } else if (bytes >= 1024 && bytes < 1048576) {
      return `${(bytes / 1024).toFixed(1)} KB`;
    } else if (bytes >= 1048576) {
      return `${(bytes / 1048576).toFixed(1)} MB`;
    }


  }
}
