<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Foundation\Auth\User as Authenticatable;
use Illuminate\Notifications\Notifiable;
use Laravel\Sanctum\HasApiTokens;

class User extends Authenticatable
{
    use HasFactory, Notifiable, HasApiTokens;

    // Fillable fields to allow mass assignment
    protected $fillable = [
        'name',
        'email',
        'password',
        'role', // Assuming you have a role column
    ];

    // One-to-Many relationship with Attendances
    public function attendances()
    {
        return $this->hasMany(Attendance::class);
    }

    // One-to-One relationship with AttendanceSummary
    public function attendanceSummary()
    {
        return $this->hasOne(AttendanceSummary::class);
    }
}
