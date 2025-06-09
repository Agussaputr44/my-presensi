<?php

namespace App\Console\Commands;

use App\Models\Attendance;
use Illuminate\Console\Command;
use App\Models\User;

class AttendanceCheck extends Command
{
    /**
     * The name and signature of the console command.
     *
     * @var string
     */
    protected $signature = 'attendance:check';

    /**
     * The console command description.
     *
     * @var string
     */
    protected $description = 'Check and update attendance for all users';

    /**
     * Execute the console command.
     *
     * @return void
     */
    public function handle()
    {
        // Logic to check and update attendance for all users
        $users = User::all();  // Get all users

        foreach ($users as $user) {
            // Example logic to check attendance
            Attendance::trackUserAttendance($user->id);
        }

        $this->info('Attendance check completed!');
    }
}
