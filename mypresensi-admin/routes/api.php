<?php
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Route;
use App\Http\Controllers\API\UserController;
use App\Http\Controllers\API\AbsensiController;
use App\Http\Controllers\API\LokasiController;
use App\Http\Controllers\API\UserDataController;

Route::prefix('v1')->group(function () {
    // Public routes for registration and login
    Route::post('/register', [UserController::class, 'register'])->name('register');
    Route::post('/login', [UserController::class, 'login'])->name('login');

    // Routes that require authentication
    Route::middleware('auth:sanctum')->group(function () {
        // User routes
        Route::get('/user', [UserController::class, 'user'])->name('user');
        Route::post('/logout', [UserController::class, 'logout'])->name('logout');

        // Absensi routes
        Route::apiResource('absensi', AbsensiController::class)->except(['create', 'edit']);
        Route::get('/user/history', [UserController::class, 'getHistory'])->name('absensi.history'); 

        // Lokasi routes
        Route::get('/lokasi', [LokasiController::class, 'index'])->name('lokasi.index');

        // Additional user data route
    });
});
