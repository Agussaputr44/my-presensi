<?php
namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;
use App\Models\Attendance;

class AttendanceSummary extends Model
{
    use HasFactory;

    protected $fillable = [
        'user_id', 'month', 'year', 
        'present_count', 'sick_count', 
        'leave_count', 'late_count', 'alpa_count',
    ];

    public function calculateSummary($userId, $month, $year)
    {
        // Ambil semua data absensi berdasarkan user_id, bulan, dan tahun
        $attendances = Attendance::where('user_id', $userId)
            ->whereYear('created_at', $year)
            ->whereMonth('created_at', $month)
            ->get();

        // Hitung jumlah untuk setiap tipe absensi
        $presentCount = $attendances->where('type', 'hadir')->count();
        $sickCount = $attendances->where('type', 'sakit')->count();
        $leaveCount = $attendances->where('type', 'izin')->count();
        $lateCount = $attendances->where('type', 'terlambat')->count();
        $alpaCount = $attendances->where('type', 'alpa')->count();
        
        // Hitung jumlah absensi dengan fake GPS

        return [
            'present_count' => $presentCount,
            'sick_count' => $sickCount,
            'leave_count' => $leaveCount,
            'late_count' => $lateCount,
            'alpa_count' => $alpaCount,
        ];
    }

    protected static function booted()
    {
        static::creating(function ($model) {
            $summary = $model->calculateSummary($model->user_id, $model->month, $model->year);
            $model->fill($summary);
        });

        static::updating(function ($model) {
            $summary = $model->calculateSummary($model->user_id, $model->month, $model->year);
            $model->fill($summary);
        });
    }

    public function user()
    {
        return $this->belongsTo(User::class);
    }
}
