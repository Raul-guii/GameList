import { CommonModule } from '@angular/common';
import { Component, Input, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';

import { Comment } from '../../models/comment.model';
import { CommentService } from '../../services/comment.service';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-comments',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './comments.component.html',
  styleUrl: './comments.component.css'
})
export class CommentsComponent implements OnInit {

  @Input({ required: true })
  gameExternalId!: number;

  comments: Comment[] = [];

  newComment = '';

  loading = false;

  editingCommentId: number | null = null;
  editingText = '';

  constructor(
    private commentService: CommentService,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadComments();
  }

  get isLoggedIn(): boolean {
    return this.authService.isLoggedIn();
  }

  loadComments(): void {
    this.loading = true;

    this.commentService
      .getCommentsByGame(this.gameExternalId)
      .subscribe({
        next: (comments) => {
          this.comments = comments;
          this.loading = false;
        },
        error: () => {
          this.loading = false;
        }
      });
  }

  createComment(): void {
    if (!this.authService.isLoggedIn()) {
      this.router.navigate(['/login']);
      return;
    }

    if (!this.newComment.trim()) return;

    this.commentService.createComment(this.gameExternalId, { content: this.newComment })
      .subscribe({
        next: () => {
          this.newComment = '';
          this.loadComments();
        },
        error: (err) => {
          console.error('Error posting comment:', err);
        }
      });
  }

  deleteComment(id: number): void {
    if (!confirm('Delete this comment?')) return;

    this.commentService.deleteComment(id).subscribe({
      next: () => this.loadComments(),
      error: (err) => {
        console.error('Error deleting comment:', err);
        alert('Could not delete the comment. Please try again.');
      }
    });
  }

  startEdit(comment: Comment): void {
    this.editingCommentId = comment.id;
    this.editingText = comment.content;
  }

  cancelEdit(): void {
    this.editingCommentId = null;
    this.editingText = '';
  }

  saveEdit(commentId: number): void {
    if (!this.editingText.trim()) return;

    this.commentService.updateComment(commentId, { content: this.editingText }).subscribe({
      next: () => {
        this.cancelEdit();
        this.loadComments();
      },
      error: (err) => {
        console.error('Error editing comment:', err);
        alert('Could not edit the comment. Please check the content and try again.');
      }
    });
  }

  isOwner(comment: Comment): boolean {
    return comment.username === this.authService.getCurrentUsername();
  }
}