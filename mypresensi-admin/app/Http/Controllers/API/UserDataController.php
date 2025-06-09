<?php

namespace App\Http\Controllers\API;

use App\Http\Controllers\Controller;
use Illuminate\Http\Request;
use App\Models\Attendance;
use Illuminate\Support\Facades\Log;

class UserDataController extends Controller
{
    public function getByUserId()
    {
        $userId = \Illuminate\Support\Facades\Auth::user()->id; 
    
        Log::info('Fetching attendance for user_id:', ['user_id' => $userId]);
    
        $attendance = Attendance::where('user_id', $userId)
        ->orderBy('created_at', 'desc')
        ->get();
        
        if ($attendance->isEmpty()) {
            return response()->json([
                'message' => 'No attendance records found for this user',
                'query_result' => $attendance
            ], 404);
        }
    
        return response()->json([
            'message' => 'Success',
            'data' => $attendance
        ], 200);
    }}
