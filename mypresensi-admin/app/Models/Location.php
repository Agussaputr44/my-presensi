<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class Location extends Model
{
    use HasFactory;

    // Define fillable fields to allow mass assignment
    protected $fillable = [
        'name',           // Name of the location
        'alamat',         // Address of the location
        'keterangan',     // Description of the location
    ];

    // One-to-Many relationship with Attendances
    public function attendances()
    {
        return $this->hasMany(Attendance::class);
    }
}
