<?php

namespace App\Filament\Widgets;

use App\Models\Attendance;
use App\Models\User;
use Filament\Widgets\StatsOverviewWidget as BaseWidget;
use Filament\Widgets\StatsOverviewWidget\Stat;
use Carbon\Carbon;

class AaStats extends BaseWidget
{
    protected function getStats(): array
    {
        $today = Carbon::today(); // Mendapatkan tanggal hari ini

        // Hitung jumlah kehadiran berdasarkan type (hadir, izin, sakit)
        $hadir = Attendance::whereDate('created_at', $today)->where('type', 'hadir')->count();
        $izin = Attendance::whereDate('created_at', $today)->where('type', 'izin')->count();
        $sakit = Attendance::whereDate('created_at', $today)->where('type', 'sakit')->count();

        // Total karyawan
        $totalUsers = User::count(); 

        // Hitung jumlah alpa
        $alpa = $totalUsers - ($hadir + $izin + $sakit);

        return [
            Stat::make('Hadir', $hadir)
                ->description('Karyawan hadir hari ini')
                ->icon('heroicon-o-check-circle')
                ->color('success'),

            Stat::make('Izin', $izin)
                ->description('Karyawan izin hari ini')
                ->icon('heroicon-o-user-circle')
                ->color('warning'),

            Stat::make('Sakit', $sakit)
                ->description('Karyawan sakit hari ini')
                ->icon('heroicon-o-heart')
                ->color('secondary'),

            Stat::make('Alpa', $alpa)
                ->description('Karyawan tidak hadir tanpa keterangan/belum absensi')
                ->icon('heroicon-o-x-circle')
                ->color('danger'),
        ];
    }
}