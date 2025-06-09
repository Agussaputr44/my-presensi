<?php

namespace Database\Seeders;

use App\Models\User;
use App\Models\Location;
// use Illuminate\Database\Console\Seeds\WithoutModelEvents;
use Illuminate\Database\Seeder;
use Illuminate\Support\Facades\Hash;

class DatabaseSeeder extends Seeder
{
    /**
     * Seed the application's database.
     */
    public function run(): void
    {
        // User::factory(10)->create();

        User::factory()->create([
            'name' => 'mypresesi',
            'email' => 'mypresensi@admin.com',
            'password' => Hash::make('12345678'),
            'role' => 'admin',
        ]);

        Location::create([
            'name'       => 'SDN 1 Bantan',
            'alamat'     => 'Jalan Jend Sudirman, Selatbaru',
            'keterangan' => 'Sekolah Dasar',
        ]);
    }
}
